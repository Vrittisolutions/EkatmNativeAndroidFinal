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
uniform highp mat4 matProj;
uniform highp mat4 matWorld;
uniform highp mat4 matWorldView;
uniform sampler2D HeightMapTexture;
uniform highp vec4 TextureDimensionsInv;
uniform highp vec4 MeshCellOfssetScalePosition;
uniform highp vec4 MeshCellOfssetScaleTexCoord;
uniform highp vec4 MeshCellOfssetScaleHeights;
uniform highp vec4 ClipPlanes[6];
uniform sGridParams GridParams;
attribute highp vec4 vPosition;
attribute highp vec4 vTexCoord0;
varying highp vec4 xlv_TEXCOORD0;
varying highp vec4 xlv_TEXCOORD1;
varying highp vec3 xlv_TEXCOORD2;
varying highp vec4 xlv_TEXCOORD4;
varying highp float xlv_TEXCOORD6;
varying highp vec4 xlv_TEXCOORD7;
varying highp vec4 xlv_COLOR4;
varying highp vec2 xlv_COLOR5;
void main ()
{
  highp vec4 tmpvar_1;
  highp vec4 tmpvar_2;
  tmpvar_1.w = vPosition.w;
  tmpvar_2.zw = vTexCoord0.zw;
  highp vec4 tmpvar_3;
  highp vec4 tmpvar_4;
  highp vec4 tmpvar_5;
  highp vec4 tmpvar_6;
  highp vec2 tmpvar_7;
  highp mat4 tmpvar_8;
  tmpvar_8 = (matWorldView * matProj);
  tmpvar_1.xz = ((vPosition.xz * MeshCellOfssetScalePosition.xy) + MeshCellOfssetScalePosition.zw);
  highp vec2 _texCoord_9;
  _texCoord_9 = ((vTexCoord0.xy * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  _texCoord_9 = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, _texCoord_9);
  lowp vec4 tmpvar_10;
  tmpvar_10 = texture2DLod (HeightMapTexture, _texCoord_9, 0.0);
  tmpvar_1.y = (tmpvar_10.x * vPosition.w);
  highp vec3 tmpvar_11;
  highp vec3 sampledNormal_12;
  highp vec3 tmpvar_13;
  tmpvar_13.z = 0.0;
  tmpvar_13.xy = TextureDimensionsInv.xy;
  highp float tmpvar_14;
  highp vec2 _texCoord_15;
  _texCoord_15 = (vTexCoord0.xy - tmpvar_13.xz);
  _texCoord_15 = ((_texCoord_15 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_16;
  tmpvar_16 = texture2DLod (HeightMapTexture, _texCoord_15, 0.0);
  tmpvar_14 = tmpvar_16.x;
  highp float tmpvar_17;
  highp vec2 _texCoord_18;
  _texCoord_18 = (vTexCoord0.xy + tmpvar_13.xz);
  _texCoord_18 = ((_texCoord_18 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_19;
  tmpvar_19 = texture2DLod (HeightMapTexture, _texCoord_18, 0.0);
  tmpvar_17 = tmpvar_19.x;
  highp float tmpvar_20;
  highp vec2 _texCoord_21;
  _texCoord_21 = (vTexCoord0.xy - tmpvar_13.zy);
  _texCoord_21 = ((_texCoord_21 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_22;
  tmpvar_22 = texture2DLod (HeightMapTexture, _texCoord_21, 0.0);
  tmpvar_20 = tmpvar_22.x;
  highp float tmpvar_23;
  highp vec2 _texCoord_24;
  _texCoord_24 = (vTexCoord0.xy + tmpvar_13.zy);
  _texCoord_24 = ((_texCoord_24 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_25;
  tmpvar_25 = texture2DLod (HeightMapTexture, _texCoord_24, 0.0);
  tmpvar_23 = tmpvar_25.x;
  sampledNormal_12.x = (((tmpvar_14 * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y) - ((tmpvar_17 * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y));
  sampledNormal_12.y = 2.0;
  sampledNormal_12.z = (((tmpvar_20 * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y) - ((tmpvar_23 * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y));
  highp vec3 tmpvar_26;
  tmpvar_26 = normalize(sampledNormal_12);
  sampledNormal_12 = tmpvar_26;
  highp mat3 tmpvar_27;
  tmpvar_27[0] = matWorld[0].xyz;
  tmpvar_27[1] = matWorld[1].xyz;
  tmpvar_27[2] = matWorld[2].xyz;
  tmpvar_11 = (tmpvar_26 * tmpvar_27);
  highp vec2 _texCoord_28;
  _texCoord_28 = ((vTexCoord0.zw * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  _texCoord_28 = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, _texCoord_28);
  lowp vec4 tmpvar_29;
  tmpvar_29 = texture2DLod (HeightMapTexture, _texCoord_28, 0.0);
  highp float tmpvar_30;
  tmpvar_30 = GridParams.Params.w;
  highp float tmpvar_31;
  tmpvar_31 = mix (tmpvar_1.y, (tmpvar_29.x * vPosition.w), tmpvar_30);
  tmpvar_2.xy = mix (vTexCoord0.xy, vTexCoord0.zw, vec2(tmpvar_30));
  tmpvar_5 = tmpvar_2;
  tmpvar_2.xy = clamp (tmpvar_2.xy, 0.0, 1.0);
  tmpvar_2.xy = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, tmpvar_2.xy);
  tmpvar_2.xy = ((tmpvar_2.xy * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  tmpvar_1.y = ((tmpvar_1.y * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y);
  tmpvar_4 = tmpvar_1;
  tmpvar_1.xz = tmpvar_1.xz;
  highp vec4 tmpvar_32;
  tmpvar_32 = (tmpvar_1 * matWorld);
  bvec4 tmpvar_33;
  tmpvar_33 = bvec4(ClipPlanes[0]);
  bool tmpvar_34;
  tmpvar_34 = any(tmpvar_33);
  highp float tmpvar_35;
  if (tmpvar_34) {
    highp vec4 tmpvar_36;
    tmpvar_36.w = 1.0;
    tmpvar_36.xyz = tmpvar_32.xyz;
    tmpvar_35 = dot (tmpvar_36, ClipPlanes[0]);
  } else {
    tmpvar_35 = 1.0;
  };
  tmpvar_6.x = tmpvar_35;
  bvec4 tmpvar_37;
  tmpvar_37 = bvec4(ClipPlanes[1]);
  bool tmpvar_38;
  tmpvar_38 = any(tmpvar_37);
  highp float tmpvar_39;
  if (tmpvar_38) {
    highp vec4 tmpvar_40;
    tmpvar_40.w = 1.0;
    tmpvar_40.xyz = tmpvar_32.xyz;
    tmpvar_39 = dot (tmpvar_40, ClipPlanes[1]);
  } else {
    tmpvar_39 = 1.0;
  };
  tmpvar_6.y = tmpvar_39;
  bvec4 tmpvar_41;
  tmpvar_41 = bvec4(ClipPlanes[2]);
  bool tmpvar_42;
  tmpvar_42 = any(tmpvar_41);
  highp float tmpvar_43;
  if (tmpvar_42) {
    highp vec4 tmpvar_44;
    tmpvar_44.w = 1.0;
    tmpvar_44.xyz = tmpvar_32.xyz;
    tmpvar_43 = dot (tmpvar_44, ClipPlanes[2]);
  } else {
    tmpvar_43 = 1.0;
  };
  tmpvar_6.z = tmpvar_43;
  bvec4 tmpvar_45;
  tmpvar_45 = bvec4(ClipPlanes[3]);
  bool tmpvar_46;
  tmpvar_46 = any(tmpvar_45);
  highp float tmpvar_47;
  if (tmpvar_46) {
    highp vec4 tmpvar_48;
    tmpvar_48.w = 1.0;
    tmpvar_48.xyz = tmpvar_32.xyz;
    tmpvar_47 = dot (tmpvar_48, ClipPlanes[3]);
  } else {
    tmpvar_47 = 1.0;
  };
  tmpvar_6.w = tmpvar_47;
  bvec4 tmpvar_49;
  tmpvar_49 = bvec4(ClipPlanes[4]);
  bool tmpvar_50;
  tmpvar_50 = any(tmpvar_49);
  highp float tmpvar_51;
  if (tmpvar_50) {
    highp vec4 tmpvar_52;
    tmpvar_52.w = 1.0;
    tmpvar_52.xyz = tmpvar_32.xyz;
    tmpvar_51 = dot (tmpvar_52, ClipPlanes[4]);
  } else {
    tmpvar_51 = 1.0;
  };
  tmpvar_7.x = tmpvar_51;
  bvec4 tmpvar_53;
  tmpvar_53 = bvec4(ClipPlanes[5]);
  bool tmpvar_54;
  tmpvar_54 = any(tmpvar_53);
  highp float tmpvar_55;
  if (tmpvar_54) {
    highp vec4 tmpvar_56;
    tmpvar_56.w = 1.0;
    tmpvar_56.xyz = tmpvar_32.xyz;
    tmpvar_55 = dot (tmpvar_56, ClipPlanes[5]);
  } else {
    tmpvar_55 = 1.0;
  };
  tmpvar_7.y = tmpvar_55;
  tmpvar_1.w = 1.0;
  tmpvar_3.xy = tmpvar_2.xy;
  tmpvar_3.zw = tmpvar_2.zw;
  gl_Position = (tmpvar_1 * tmpvar_8);
  xlv_TEXCOORD0 = tmpvar_3;
  xlv_TEXCOORD1 = tmpvar_4;
  xlv_TEXCOORD2 = tmpvar_11;
  xlv_TEXCOORD4 = (tmpvar_1 * matWorld);
  xlv_TEXCOORD6 = tmpvar_31;
  xlv_TEXCOORD7 = tmpvar_5;
  xlv_COLOR4 = tmpvar_6;
  xlv_COLOR5 = tmpvar_7;
}

