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
uniform highp vec4 ClipPlanes[6];
uniform sGridParams GridParams;
uniform highp vec4 TextureDimensionsInv;
uniform highp vec4 MeshCellOfssetScalePosition;
uniform highp vec4 MeshCellOfssetScaleTexCoord;
uniform highp vec4 MeshCellOfssetScaleHeights;
uniform sampler2D HeightMapTexture;
attribute highp vec4 vPosition;
attribute highp vec4 vTexCoord0;
varying highp vec4 xlv_TEXCOORD0;
varying highp vec4 xlv_TEXCOORD1;
varying highp vec3 xlv_TEXCOORD2;
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
  highp vec4 rgba_11;
  rgba_11 = tmpvar_10.zyxw;
  tmpvar_1.y = (((
    dot (rgba_11.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8))
   * PackedFloatParams.y) + PackedFloatParams.x) * vPosition.w);
  highp vec3 tmpvar_12;
  highp vec3 sampledNormal_13;
  highp vec3 tmpvar_14;
  tmpvar_14.z = 0.0;
  tmpvar_14.xy = TextureDimensionsInv.xy;
  highp vec2 _texCoord_15;
  _texCoord_15 = (vTexCoord0.xy - tmpvar_14.xz);
  _texCoord_15 = ((_texCoord_15 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_16;
  tmpvar_16 = texture2DLod (HeightMapTexture, _texCoord_15, 0.0);
  highp vec4 rgba_17;
  rgba_17 = tmpvar_16.zyxw;
  highp vec2 _texCoord_18;
  _texCoord_18 = (vTexCoord0.xy + tmpvar_14.xz);
  _texCoord_18 = ((_texCoord_18 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_19;
  tmpvar_19 = texture2DLod (HeightMapTexture, _texCoord_18, 0.0);
  highp vec4 rgba_20;
  rgba_20 = tmpvar_19.zyxw;
  highp vec2 _texCoord_21;
  _texCoord_21 = (vTexCoord0.xy - tmpvar_14.zy);
  _texCoord_21 = ((_texCoord_21 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_22;
  tmpvar_22 = texture2DLod (HeightMapTexture, _texCoord_21, 0.0);
  highp vec4 rgba_23;
  rgba_23 = tmpvar_22.zyxw;
  highp vec2 _texCoord_24;
  _texCoord_24 = (vTexCoord0.xy + tmpvar_14.zy);
  _texCoord_24 = ((_texCoord_24 * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  lowp vec4 tmpvar_25;
  tmpvar_25 = texture2DLod (HeightMapTexture, _texCoord_24, 0.0);
  highp vec4 rgba_26;
  rgba_26 = tmpvar_25.zyxw;
  sampledNormal_13.x = (((
    ((dot (rgba_17.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y) + PackedFloatParams.x)
   * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y) - ((
    ((dot (rgba_20.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y) + PackedFloatParams.x)
   * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y));
  sampledNormal_13.y = 2.0;
  sampledNormal_13.z = (((
    ((dot (rgba_23.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y) + PackedFloatParams.x)
   * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y) - ((
    ((dot (rgba_26.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y) + PackedFloatParams.x)
   * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y));
  highp vec3 tmpvar_27;
  tmpvar_27 = normalize(sampledNormal_13);
  sampledNormal_13 = tmpvar_27;
  highp mat3 tmpvar_28;
  tmpvar_28[0] = matWorld[0].xyz;
  tmpvar_28[1] = matWorld[1].xyz;
  tmpvar_28[2] = matWorld[2].xyz;
  tmpvar_12 = (tmpvar_27 * tmpvar_28);
  highp vec2 _texCoord_29;
  _texCoord_29 = ((vTexCoord0.zw * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  _texCoord_29 = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, _texCoord_29);
  lowp vec4 tmpvar_30;
  tmpvar_30 = texture2DLod (HeightMapTexture, _texCoord_29, 0.0);
  highp vec4 rgba_31;
  rgba_31 = tmpvar_30.zyxw;
  highp float tmpvar_32;
  tmpvar_32 = GridParams.Params.w;
  highp float tmpvar_33;
  tmpvar_33 = mix (tmpvar_1.y, ((
    (dot (rgba_31.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y)
   + PackedFloatParams.x) * vPosition.w), tmpvar_32);
  tmpvar_2.xy = mix (vTexCoord0.xy, vTexCoord0.zw, vec2(tmpvar_32));
  tmpvar_5 = tmpvar_2;
  tmpvar_2.xy = clamp (tmpvar_2.xy, 0.0, 1.0);
  tmpvar_2.xy = mix (TextureDimensionsInv.xy, TextureDimensionsInv.zw, tmpvar_2.xy);
  tmpvar_2.xy = ((tmpvar_2.xy * MeshCellOfssetScaleTexCoord.xy) + MeshCellOfssetScaleTexCoord.zw);
  tmpvar_1.y = ((tmpvar_1.y * MeshCellOfssetScaleHeights.x) + MeshCellOfssetScaleHeights.y);
  tmpvar_4 = tmpvar_1;
  tmpvar_1.xz = tmpvar_1.xz;
  highp vec4 tmpvar_34;
  tmpvar_34 = (tmpvar_1 * matWorld);
  bvec4 tmpvar_35;
  tmpvar_35 = bvec4(ClipPlanes[0]);
  bool tmpvar_36;
  tmpvar_36 = any(tmpvar_35);
  highp float tmpvar_37;
  if (tmpvar_36) {
    highp vec4 tmpvar_38;
    tmpvar_38.w = 1.0;
    tmpvar_38.xyz = tmpvar_34.xyz;
    tmpvar_37 = dot (tmpvar_38, ClipPlanes[0]);
  } else {
    tmpvar_37 = 1.0;
  };
  tmpvar_6.x = tmpvar_37;
  bvec4 tmpvar_39;
  tmpvar_39 = bvec4(ClipPlanes[1]);
  bool tmpvar_40;
  tmpvar_40 = any(tmpvar_39);
  highp float tmpvar_41;
  if (tmpvar_40) {
    highp vec4 tmpvar_42;
    tmpvar_42.w = 1.0;
    tmpvar_42.xyz = tmpvar_34.xyz;
    tmpvar_41 = dot (tmpvar_42, ClipPlanes[1]);
  } else {
    tmpvar_41 = 1.0;
  };
  tmpvar_6.y = tmpvar_41;
  bvec4 tmpvar_43;
  tmpvar_43 = bvec4(ClipPlanes[2]);
  bool tmpvar_44;
  tmpvar_44 = any(tmpvar_43);
  highp float tmpvar_45;
  if (tmpvar_44) {
    highp vec4 tmpvar_46;
    tmpvar_46.w = 1.0;
    tmpvar_46.xyz = tmpvar_34.xyz;
    tmpvar_45 = dot (tmpvar_46, ClipPlanes[2]);
  } else {
    tmpvar_45 = 1.0;
  };
  tmpvar_6.z = tmpvar_45;
  bvec4 tmpvar_47;
  tmpvar_47 = bvec4(ClipPlanes[3]);
  bool tmpvar_48;
  tmpvar_48 = any(tmpvar_47);
  highp float tmpvar_49;
  if (tmpvar_48) {
    highp vec4 tmpvar_50;
    tmpvar_50.w = 1.0;
    tmpvar_50.xyz = tmpvar_34.xyz;
    tmpvar_49 = dot (tmpvar_50, ClipPlanes[3]);
  } else {
    tmpvar_49 = 1.0;
  };
  tmpvar_6.w = tmpvar_49;
  bvec4 tmpvar_51;
  tmpvar_51 = bvec4(ClipPlanes[4]);
  bool tmpvar_52;
  tmpvar_52 = any(tmpvar_51);
  highp float tmpvar_53;
  if (tmpvar_52) {
    highp vec4 tmpvar_54;
    tmpvar_54.w = 1.0;
    tmpvar_54.xyz = tmpvar_34.xyz;
    tmpvar_53 = dot (tmpvar_54, ClipPlanes[4]);
  } else {
    tmpvar_53 = 1.0;
  };
  tmpvar_7.x = tmpvar_53;
  bvec4 tmpvar_55;
  tmpvar_55 = bvec4(ClipPlanes[5]);
  bool tmpvar_56;
  tmpvar_56 = any(tmpvar_55);
  highp float tmpvar_57;
  if (tmpvar_56) {
    highp vec4 tmpvar_58;
    tmpvar_58.w = 1.0;
    tmpvar_58.xyz = tmpvar_34.xyz;
    tmpvar_57 = dot (tmpvar_58, ClipPlanes[5]);
  } else {
    tmpvar_57 = 1.0;
  };
  tmpvar_7.y = tmpvar_57;
  tmpvar_1.w = 1.0;
  tmpvar_3.xy = tmpvar_2.xy;
  tmpvar_3.zw = tmpvar_2.zw;
  gl_Position = (tmpvar_1 * tmpvar_8);
  xlv_TEXCOORD0 = tmpvar_3;
  xlv_TEXCOORD1 = tmpvar_4;
  xlv_TEXCOORD2 = tmpvar_12;
  xlv_TEXCOORD6 = tmpvar_33;
  xlv_TEXCOORD7 = tmpvar_5;
  xlv_COLOR4 = tmpvar_6;
  xlv_COLOR5 = tmpvar_7;
}

