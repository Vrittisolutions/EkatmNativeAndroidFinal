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
uniform highp vec4 PackedFloatParams;
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
varying highp vec4 xlv_TEXCOORD5;
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
  highp vec4 tmpvar_7;
  highp vec2 tmpvar_8;
  highp mat4 tmpvar_9;
  tmpvar_9 = (matWorldView * matProj);
  tmpvar_1.xz = ((vPosition.xz * MeshCellOfssetScalePosition.xy) + MeshCellOfssetScalePosition.zw);
  highp vec2 _texCoord_10;
  _texCoord_10 = ((vTexCoord0.xy * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  _texCoord_10 = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, _texCoord_10);
  lowp vec4 tmpvar_11;
  tmpvar_11 = texture2DLod (HeightMapTexture, _texCoord_10, 0.0);
  highp vec4 rgba_12;
  rgba_12 = tmpvar_11.zyxw;
  tmpvar_1.y = (((
    dot (rgba_12.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8))
   * PackedFloatParams.y) + PackedFloatParams.x) * vPosition.w);
  highp vec3 tmpvar_13;
  highp vec3 sampledNormal_14;
  highp vec3 tmpvar_15;
  tmpvar_15.z = 0.0;
  tmpvar_15.xy = TextureDimensionsInv.xy;
  highp vec2 _texCoord_16;
  _texCoord_16 = (vTexCoord0.xy - tmpvar_15.xz);
  _texCoord_16 = ((_texCoord_16 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_17;
  tmpvar_17 = texture2DLod (HeightMapTexture, _texCoord_16, 0.0);
  highp vec4 rgba_18;
  rgba_18 = tmpvar_17.zyxw;
  highp vec2 _texCoord_19;
  _texCoord_19 = (vTexCoord0.xy + tmpvar_15.xz);
  _texCoord_19 = ((_texCoord_19 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_20;
  tmpvar_20 = texture2DLod (HeightMapTexture, _texCoord_19, 0.0);
  highp vec4 rgba_21;
  rgba_21 = tmpvar_20.zyxw;
  highp vec2 _texCoord_22;
  _texCoord_22 = (vTexCoord0.xy - tmpvar_15.zy);
  _texCoord_22 = ((_texCoord_22 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_23;
  tmpvar_23 = texture2DLod (HeightMapTexture, _texCoord_22, 0.0);
  highp vec4 rgba_24;
  rgba_24 = tmpvar_23.zyxw;
  highp vec2 _texCoord_25;
  _texCoord_25 = (vTexCoord0.xy + tmpvar_15.zy);
  _texCoord_25 = ((_texCoord_25 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_26;
  tmpvar_26 = texture2DLod (HeightMapTexture, _texCoord_25, 0.0);
  highp vec4 rgba_27;
  rgba_27 = tmpvar_26.zyxw;
  sampledNormal_14.x = (((
    ((dot (rgba_18.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y) + PackedFloatParams.x)
   * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y) - ((
    ((dot (rgba_21.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y) + PackedFloatParams.x)
   * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y));
  sampledNormal_14.y = 2.0;
  sampledNormal_14.z = (((
    ((dot (rgba_24.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y) + PackedFloatParams.x)
   * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y) - ((
    ((dot (rgba_27.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y) + PackedFloatParams.x)
   * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y));
  highp vec3 tmpvar_28;
  tmpvar_28 = normalize(sampledNormal_14);
  sampledNormal_14 = tmpvar_28;
  highp mat3 tmpvar_29;
  tmpvar_29[0] = matWorld[0].xyz;
  tmpvar_29[1] = matWorld[1].xyz;
  tmpvar_29[2] = matWorld[2].xyz;
  tmpvar_13 = (tmpvar_28 * tmpvar_29);
  highp vec2 _texCoord_30;
  _texCoord_30 = ((vTexCoord0.zw * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  _texCoord_30 = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, _texCoord_30);
  lowp vec4 tmpvar_31;
  tmpvar_31 = texture2DLod (HeightMapTexture, _texCoord_30, 0.0);
  highp vec4 rgba_32;
  rgba_32 = tmpvar_31.zyxw;
  highp float tmpvar_33;
  tmpvar_33 = GridParams.Params.w;
  highp float tmpvar_34;
  tmpvar_34 = mix (tmpvar_1.y, ((
    (dot (rgba_32.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y)
   + PackedFloatParams.x) * vPosition.w), tmpvar_33);
  tmpvar_2.xy = mix (vTexCoord0.xy, vTexCoord0.zw, vec2(tmpvar_33));
  tmpvar_6 = tmpvar_2;
  tmpvar_2.xy = clamp (tmpvar_2.xy, 0.0, 1.0);
  tmpvar_2.xy = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, tmpvar_2.xy);
  tmpvar_2.xy = ((tmpvar_2.xy * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  tmpvar_1.y = ((tmpvar_1.y * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y);
  tmpvar_5 = tmpvar_1;
  tmpvar_1.xz = tmpvar_1.xz;
  highp vec4 tmpvar_35;
  tmpvar_35 = (tmpvar_1 * matWorld);
  bvec4 tmpvar_36;
  tmpvar_36 = bvec4(ClipPlanes[0]);
  bool tmpvar_37;
  tmpvar_37 = any(tmpvar_36);
  highp float tmpvar_38;
  if (tmpvar_37) {
    highp vec4 tmpvar_39;
    tmpvar_39.w = 1.0;
    tmpvar_39.xyz = tmpvar_35.xyz;
    tmpvar_38 = dot (tmpvar_39, ClipPlanes[0]);
  } else {
    tmpvar_38 = 1.0;
  };
  tmpvar_7.x = tmpvar_38;
  bvec4 tmpvar_40;
  tmpvar_40 = bvec4(ClipPlanes[1]);
  bool tmpvar_41;
  tmpvar_41 = any(tmpvar_40);
  highp float tmpvar_42;
  if (tmpvar_41) {
    highp vec4 tmpvar_43;
    tmpvar_43.w = 1.0;
    tmpvar_43.xyz = tmpvar_35.xyz;
    tmpvar_42 = dot (tmpvar_43, ClipPlanes[1]);
  } else {
    tmpvar_42 = 1.0;
  };
  tmpvar_7.y = tmpvar_42;
  bvec4 tmpvar_44;
  tmpvar_44 = bvec4(ClipPlanes[2]);
  bool tmpvar_45;
  tmpvar_45 = any(tmpvar_44);
  highp float tmpvar_46;
  if (tmpvar_45) {
    highp vec4 tmpvar_47;
    tmpvar_47.w = 1.0;
    tmpvar_47.xyz = tmpvar_35.xyz;
    tmpvar_46 = dot (tmpvar_47, ClipPlanes[2]);
  } else {
    tmpvar_46 = 1.0;
  };
  tmpvar_7.z = tmpvar_46;
  bvec4 tmpvar_48;
  tmpvar_48 = bvec4(ClipPlanes[3]);
  bool tmpvar_49;
  tmpvar_49 = any(tmpvar_48);
  highp float tmpvar_50;
  if (tmpvar_49) {
    highp vec4 tmpvar_51;
    tmpvar_51.w = 1.0;
    tmpvar_51.xyz = tmpvar_35.xyz;
    tmpvar_50 = dot (tmpvar_51, ClipPlanes[3]);
  } else {
    tmpvar_50 = 1.0;
  };
  tmpvar_7.w = tmpvar_50;
  bvec4 tmpvar_52;
  tmpvar_52 = bvec4(ClipPlanes[4]);
  bool tmpvar_53;
  tmpvar_53 = any(tmpvar_52);
  highp float tmpvar_54;
  if (tmpvar_53) {
    highp vec4 tmpvar_55;
    tmpvar_55.w = 1.0;
    tmpvar_55.xyz = tmpvar_35.xyz;
    tmpvar_54 = dot (tmpvar_55, ClipPlanes[4]);
  } else {
    tmpvar_54 = 1.0;
  };
  tmpvar_8.x = tmpvar_54;
  bvec4 tmpvar_56;
  tmpvar_56 = bvec4(ClipPlanes[5]);
  bool tmpvar_57;
  tmpvar_57 = any(tmpvar_56);
  highp float tmpvar_58;
  if (tmpvar_57) {
    highp vec4 tmpvar_59;
    tmpvar_59.w = 1.0;
    tmpvar_59.xyz = tmpvar_35.xyz;
    tmpvar_58 = dot (tmpvar_59, ClipPlanes[5]);
  } else {
    tmpvar_58 = 1.0;
  };
  tmpvar_8.y = tmpvar_58;
  tmpvar_1.w = 1.0;
  tmpvar_3 = (tmpvar_1 * tmpvar_9);
  tmpvar_4.xy = tmpvar_2.xy;
  tmpvar_4.zw = tmpvar_2.zw;
  gl_Position = tmpvar_3;
  xlv_TEXCOORD0 = tmpvar_4;
  xlv_TEXCOORD1 = tmpvar_5;
  xlv_TEXCOORD2 = tmpvar_13;
  xlv_TEXCOORD4 = (tmpvar_1 * matWorld);
  xlv_TEXCOORD5 = tmpvar_3;
  xlv_TEXCOORD6 = tmpvar_34;
  xlv_TEXCOORD7 = tmpvar_6;
  xlv_COLOR4 = tmpvar_7;
  xlv_COLOR5 = tmpvar_8;
}

