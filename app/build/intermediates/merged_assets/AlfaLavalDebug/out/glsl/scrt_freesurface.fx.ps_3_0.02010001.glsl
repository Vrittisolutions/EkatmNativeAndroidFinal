#extension GL_EXT_shader_texture_lod : enable
#extension GL_OES_standard_derivatives : enable
lowp vec4 impl_low_texture2DLodEXT(lowp sampler2D sampler, highp vec2 coord, mediump float lod)
{
#if defined(GL_EXT_shader_texture_lod)
	return texture2DLodEXT(sampler, coord, lod);
#else
	return texture2D(sampler, coord, lod);
#endif
}

precision mediump float;
struct sGradientParams {
  highp vec4 YMinMaxFactor;
  highp vec4 XMinMaxFactor;
  highp vec4 ZMinMaxFactor;
};
struct sGridParams {
  highp vec4 ContourScale;
  highp vec4 ContourOffset;
  highp vec4 ContourThickness;
  highp vec4 ContourColorX;
  highp vec4 ContourColorY;
  highp vec4 ContourColorZ;
  highp vec4 Params;
  highp vec4 SurfaceNormal;
  highp vec4 Lighting;
};
struct MaterialInfo {
  highp vec4 m_DiffuseColor;
  highp vec4 m_SpecularColor;
  highp vec2 m_SpecularPowerBumpiness;
};
uniform highp vec3 CameraPosition;
uniform highp vec4 DataPositionOffset;
uniform highp vec4 DisplacementAxesConstraints;
uniform sGradientParams GradientMinMax;
uniform sGridParams GridParams;
uniform sampler2D CellInfoTexture;
uniform highp vec4 AmbientLightColor;
uniform MaterialInfo Material;
uniform highp mat4 Light0;
uniform sampler2D DiffuseTexture;
varying highp vec4 xlv_TEXCOORD0;
varying highp vec4 xlv_TEXCOORD1;
varying highp vec3 xlv_TEXCOORD2;
varying highp float xlv_TEXCOORD6;
void main ()
{
  highp vec4 diffuseTextureColor_1;
  highp vec4 baseColor_2;
  highp vec4 finalColor_3;
  highp vec2 x_4;
  x_4 = (vec2(0.5, 0.5) - abs((xlv_TEXCOORD0.xy - vec2(0.5, 0.5))));
  bvec2 tmpvar_5;
  tmpvar_5 = lessThan (x_4, vec2(0.0, 0.0));
  if (any(tmpvar_5)) {
    discard;
  };
  highp float fAzimuthalWeight_6;
  highp float fSampleCoord_7;
  highp vec3 tmpvar_8;
  tmpvar_8 = (xlv_TEXCOORD1.xyz - DataPositionOffset.xyz);
  highp vec3 tmpvar_9;
  tmpvar_9.x = GradientMinMax.XMinMaxFactor.x;
  tmpvar_9.y = GradientMinMax.YMinMaxFactor.x;
  tmpvar_9.z = GradientMinMax.ZMinMaxFactor.x;
  highp vec3 tmpvar_10;
  tmpvar_10.x = GradientMinMax.XMinMaxFactor.y;
  tmpvar_10.y = GradientMinMax.YMinMaxFactor.y;
  tmpvar_10.z = GradientMinMax.ZMinMaxFactor.y;
  highp vec3 tmpvar_11;
  tmpvar_11.x = GradientMinMax.XMinMaxFactor.z;
  tmpvar_11.y = GradientMinMax.YMinMaxFactor.z;
  tmpvar_11.z = GradientMinMax.ZMinMaxFactor.z;
  highp float tmpvar_12;
  tmpvar_12 = GradientMinMax.YMinMaxFactor.w;
  highp float tmpvar_13;
  tmpvar_13 = GradientMinMax.ZMinMaxFactor.w;
  highp vec3 tmpvar_14;
  tmpvar_14 = clamp (((xlv_TEXCOORD1.xyz - tmpvar_9) / (tmpvar_10 - tmpvar_9)), 0.0, 1.0);
  highp vec3 tmpvar_15;
  tmpvar_15 = (tmpvar_8 * clamp (ceil(DisplacementAxesConstraints.xyz), 0.0, 1.0));
  highp float tmpvar_16;
  tmpvar_16 = dot (tmpvar_9, tmpvar_9);
  highp float tmpvar_17;
  tmpvar_17 = clamp (((
    dot (tmpvar_15, tmpvar_15)
   - tmpvar_16) / (
    dot (tmpvar_10, tmpvar_10)
   - tmpvar_16)), 0.0, 1.0);
  fSampleCoord_7 = (dot ((tmpvar_14 * 
    (tmpvar_14 * (3.0 - (2.0 * tmpvar_14)))
  ), tmpvar_11) + ((tmpvar_17 * 
    (tmpvar_17 * (3.0 - (2.0 * tmpvar_17)))
  ) * GradientMinMax.XMinMaxFactor.w));
  highp vec3 tmpvar_18;
  tmpvar_18 = normalize(tmpvar_8);
  highp vec3 tmpvar_19;
  tmpvar_19.y = 0.0;
  tmpvar_19.xz = tmpvar_18.xz;
  fAzimuthalWeight_6 = ((dot (
    normalize(tmpvar_19)
  , vec3(-1.0, 0.0, 0.0)) + 1.0) / 4.0);
  if ((tmpvar_18.z < 0.0)) {
    fAzimuthalWeight_6 = (1.0 - fAzimuthalWeight_6);
  };
  fSampleCoord_7 = (fSampleCoord_7 + (fAzimuthalWeight_6 * tmpvar_12));
  fSampleCoord_7 = (fSampleCoord_7 + ((
    (tmpvar_18.y + 1.0)
   / 2.0) * tmpvar_13));
  highp float tmpvar_20;
  tmpvar_20 = clamp (fSampleCoord_7, 0.0, 1.0);
  fSampleCoord_7 = tmpvar_20;
  highp vec2 tmpvar_21;
  tmpvar_21.y = 0.0;
  tmpvar_21.x = tmpvar_20;
  highp vec2 tmpvar_22;
  tmpvar_22 = mix (xlv_TEXCOORD0.xy, tmpvar_21, GridParams.Params.xx);
  highp vec3 tmpvar_23;
  tmpvar_23 = normalize(xlv_TEXCOORD2);
  highp vec3 tmpvar_24;
  tmpvar_24 = normalize((CameraPosition - xlv_TEXCOORD1.xyz));
  lowp vec4 tmpvar_25;
  tmpvar_25 = texture2D (DiffuseTexture, tmpvar_22);
  diffuseTextureColor_1 = tmpvar_25;
  baseColor_2 = (Material.m_DiffuseColor * diffuseTextureColor_1);
  highp vec3 tmpvar_26;
  tmpvar_26.x = xlv_TEXCOORD0.x;
  tmpvar_26.y = max (xlv_TEXCOORD6, 1e-6);
  tmpvar_26.z = xlv_TEXCOORD0.y;
  highp vec3 tmpvar_27;
  highp vec3 fH_28;
  fH_28 = ((tmpvar_26 + GridParams.ContourOffset.xyz) * GridParams.ContourScale.xyz);
  highp vec3 tmpvar_29;
  tmpvar_29 = (GridParams.ContourThickness.xyz * 0.5);
  highp vec3 tmpvar_30;
  tmpvar_30 = (abs(dFdx(fH_28)) + abs(dFdy(fH_28)));
  highp vec3 tmpvar_31;
  tmpvar_31 = clamp ((abs(
    fract(fH_28)
  ) / (tmpvar_30 * tmpvar_29)), 0.0, 1.0);
  highp vec3 tmpvar_32;
  tmpvar_32 = clamp (((1.0 - 
    abs(fract(fH_28))
  ) / (tmpvar_30 * tmpvar_29)), 0.0, 1.0);
  tmpvar_27 = ((tmpvar_32 * (tmpvar_32 * 
    (3.0 - (2.0 * tmpvar_32))
  )) * (tmpvar_31 * (tmpvar_31 * 
    (3.0 - (2.0 * tmpvar_31))
  )));
  finalColor_3 = (baseColor_2 * GridParams.Params.y);
  lowp vec4 tmpvar_33;
  tmpvar_33 = impl_low_texture2DLodEXT (CellInfoTexture, xlv_TEXCOORD0.xy, 0.0);
  highp vec4 tmpvar_34;
  tmpvar_34 = tmpvar_33;
  bvec4 tmpvar_35;
  tmpvar_35 = bvec4(tmpvar_34);
  bool tmpvar_36;
  tmpvar_36 = any(tmpvar_35);
  highp vec4 tmpvar_37;
  if (tmpvar_36) {
    tmpvar_37 = tmpvar_34;
  } else {
    tmpvar_37 = finalColor_3;
  };
  highp vec4 tmpvar_38;
  tmpvar_38.xyz = Material.m_SpecularColor.xyz;
  tmpvar_38.w = Material.m_SpecularPowerBumpiness.x;
  highp vec4 finalColor_39;
  highp vec4 tmpvar_40;
  tmpvar_40 = (tmpvar_37 * AmbientLightColor);
  highp vec4 v_41;
  v_41.x = Light0[0].y;
  v_41.y = Light0[1].y;
  v_41.z = Light0[2].y;
  v_41.w = Light0[3].y;
  highp vec4 v_42;
  v_42.x = Light0[0].x;
  v_42.y = Light0[1].x;
  v_42.z = Light0[2].x;
  v_42.w = Light0[3].x;
  highp vec3 tmpvar_43;
  tmpvar_43 = normalize(-(v_42.xyz));
  finalColor_39.xyz = (tmpvar_40 + ((
    (tmpvar_37 * max (dot (tmpvar_23, tmpvar_43), 0.0))
   + 
    (tmpvar_38 * pow (clamp (dot (
      (tmpvar_43 - (2.0 * (dot (tmpvar_23, tmpvar_43) * tmpvar_23)))
    , 
      -(normalize(tmpvar_24))
    ), 0.0, 1.0), tmpvar_38.w))
  ) * v_41)).xyz;
  finalColor_39.w = tmpvar_40.w;
  finalColor_3 = (((1.0 - GridParams.Lighting.x) * tmpvar_37) + (GridParams.Lighting.x * clamp (finalColor_39, 0.0, 1.0)));
  highp vec4 tmpvar_44;
  tmpvar_44 = mix (mix (finalColor_3, GridParams.ContourColorX, vec4((GridParams.ContourColorX.w * 
    (1.0 - tmpvar_27.x)
  ))), GridParams.ContourColorZ, vec4((GridParams.ContourColorZ.w * (1.0 - tmpvar_27.z))));
  finalColor_3.xyz = tmpvar_44.xyz;
  finalColor_3.w = (tmpvar_44.w * (baseColor_2.w * Material.m_DiffuseColor.w));
  highp vec4 tmpvar_45;
  tmpvar_45.xyz = finalColor_3.xyz;
  tmpvar_45.w = GridParams.Params.y;
  bvec4 tmpvar_46;
  tmpvar_46 = bvec4(tmpvar_45);
  bool tmpvar_47;
  tmpvar_47 = any(tmpvar_46);
  highp float tmpvar_48;
  if (tmpvar_47) {
    tmpvar_48 = (finalColor_3.w - 0.00390625);
  } else {
    tmpvar_48 = -1.0;
  };
  if ((tmpvar_48 < 0.0)) {
    discard;
  };
  gl_FragData[0] = finalColor_3;
}

